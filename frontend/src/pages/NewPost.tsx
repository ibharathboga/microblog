import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Layout } from '@/components/Layout';
import { Button } from '@/components/ui/button';
import { Textarea } from '@/components/ui/textarea';
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import { postsAPI } from '@/lib/api';
import { toast } from 'sonner';

export default function NewPost() {
  const navigate = useNavigate();
  const [content, setContent] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (content.trim().length === 0) {
      toast.error('Post cannot be empty');
      return;
    }

    if (content.length > 280) {
      toast.error('Post cannot exceed 280 characters');
      return;
    }

    setLoading(true);

    try {
      await postsAPI.createPost(content);
      toast.success('Post created!');
      navigate('/feed');
    } catch (error) {
      toast.error('Failed to create post');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Layout>
      <div className="border-x border-border min-h-screen">
        <div className="sticky top-0 z-10 border-b border-border bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
          <h1 className="p-4 text-xl font-bold">Create Post</h1>
        </div>

        <div className="p-4">
          <Card>
            <form onSubmit={handleSubmit}>
              <CardHeader>
                <CardTitle>What's on your mind?</CardTitle>
              </CardHeader>
              <CardContent>
                <Textarea
                  placeholder="Share your thoughts..."
                  value={content}
                  onChange={(e) => setContent(e.target.value)}
                  className="min-h-[200px] resize-none"
                  maxLength={280}
                />
                <div className="mt-2 flex items-center justify-between text-sm">
                  <span className={content.length > 280 ? 'text-destructive' : 'text-muted-foreground'}>
                    {content.length} / 280
                  </span>
                </div>
              </CardContent>
              <CardFooter className="flex justify-end space-x-2">
                <Button type="button" variant="outline" onClick={() => navigate(-1)}>
                  Cancel
                </Button>
                <Button type="submit" disabled={loading || content.trim().length === 0}>
                  {loading ? 'Posting...' : 'Post'}
                </Button>
              </CardFooter>
            </form>
          </Card>
        </div>
      </div>
    </Layout>
  );
}
